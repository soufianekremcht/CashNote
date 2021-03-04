package com.soufianekre.cashnotes.helper.rx;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler io();
    Scheduler computation();
    Scheduler ui();
}
