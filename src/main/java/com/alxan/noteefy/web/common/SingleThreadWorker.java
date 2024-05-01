package com.alxan.noteefy.web.common;

import com.alxan.noteefy.common.worker.AsyncWorkerImp;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.common.worker.ExecutorImp;

public class SingleThreadWorker extends AsyncWorkerImp {
    public SingleThreadWorker() {
        Executor executor = new ExecutorImp(1);
        setExecutor(executor);
    }
}
