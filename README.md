# quartz实现原理研究



## 1.任务调度原理
QuartzSchedulerThread

1.获取待执行的触发器

每次任务执行只有一个实例取得执行权限,基于qrtz_locks表，联合主键SCHED_NAME（实例名称），LOCK_NAME(锁名称)

基于DB for update实现 核心类StdRowLockSemaphore

需要设置事务手动提交,获取锁的时候先for update查询

有记录则返回说明已经获取到锁，没有则新增然后返回

然后把锁名称(TRIGGER_ACCESS)绑定到ThreadLocal，然后执行回调(获取并更新触发器状态
把qrtz_triggers状态从WAITING改成ACQUIRED
插入qrtz_fired_triggers记录,状态为ACQUIRED

)

然后提交,最后关闭连接，把锁名称从ThreadLocal移除

JobRunShell 开始执行
更改qrtz_fired_triggers记录状态为EXECUTING,
如果job是单线程的则qrtz_triggers原状态改为WAITING或ACQUIRED的改成 BLOCKED，原状态PAUSED的改成PAUSED_BLOCKED

如果不是单线程的改成WAITING

JobRunShell执行完毕删除qrtz_fired_triggers记录


## 2.集群原理(节点自动注册与失效转移)
1.ClusterManager 负责管理集群，维护集群状态，对已经获取到执行权限的触发器做失败权限释放(更改状态实现)

启动后会执行一次检查manage，如果不是第一次检查，则查询qrtz_scheduler_state表

判断最后一次检查时间有问题的节点捞取出来，并且更新自己节点的数据最后一次更新时间，

如果是第一次检查或有问题的节点>0则根据获取状态锁(STATE_ACCESS),如果是第一次检查

新增一条当前节点的qrtz_scheduler_state记录，如果不是第一次则再次捞取一次有问题的记录

然后如果有问题的记录数>0，则获取触发器锁(TRIGGER_ACCESS),

根据INSTANCE_NAME查询出qrtz_fired_triggers的job执行记录

根据qrtz_fired_triggers 的状态，

如果是锁定(BLOCKED)则把qrtz_triggers的状态改成等待(WAITING)

如果是暂停锁定(PAUSED_BLOCKED)则把qrtz_triggers的状态改成暂停(PAUSED)

如果是获得状态(ACQUIRED)则把qrtz_triggers的状态改成等待(WAITING)

删除qrtz_fired_triggers数据

删除qrtz_scheduler_state数据

ClusterManager 定时按照如下方式检查

休眠时间=检查频率(7500)-当前时间搓-最后一次检查时间搓

休眠时间=休眠时间<=0?100:休眠时间

线程休眠一会儿

执行检查manage

如果检查有问题则signalSchedulingChangeImmediately


