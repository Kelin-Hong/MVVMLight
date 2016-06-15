package com.kelin.mvvmlight.command;

import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * Created by kelin on 15-8-4.
 */
public class ReplyCommand<T> {

    private Action0 execute0;
    private Action1<T> execute1;

    private Func0<Boolean> canExecute0;

    public ReplyCommand(Action0 execute) {
        this.execute0 = execute;
    }


    public ReplyCommand(Action1<T> execute) {
        this.execute1 = execute;
    }

    /**
     *
     * @param execute callback for event
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Action0 execute, Func0<Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     *
     * @param execute callback for event,this callback need a params
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Action1<T> execute, Func0<Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }


    public void execute() {
        if (execute0 != null && canExecute0()) {
            execute0.call();
        }
    }

    private boolean canExecute0() {
        if (canExecute0 == null) {
            return true;
        }
        return canExecute0.call();
    }


    public void execute(T parameter) {
        if (execute1 != null && canExecute0()) {
            execute1.call(parameter);
        }
    }

}
