package com.kmm.laserstars.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.kmm.laserstars.databinding.AddDesginFragmentBinding;
import com.kmm.laserstars.ui.add_desgin.AddDesginViewModel;

public class TimerThread extends Thread {
    private final Activity context;
    private final AddDesginFragmentBinding binding;
    private final AddDesginViewModel viewModel;
    private static final int MAX_TIME = (int) 1e4;
    int timer; // 10 sec

    public TimerThread(Activity context, AddDesginFragmentBinding binding,
                       AddDesginViewModel viewModel) {
        this.context = context;
        this.binding = binding;
        this.viewModel = viewModel;
        this.timer = MAX_TIME;
    }

    @Override
    public void run() {
        super.run();

        try {

            while (timer > 0 && !this.isInterrupted()) {
                sleep(200);
                timer -= 200;
                context.runOnUiThread(() -> {
                    int time = (MAX_TIME - timer) / 1000; // convert from mi to sec
                    //convert time to percent
                    // 10    -> 100
                    // time  -> x
                    // x = time * 100 / 10 ==> x = (time * 10)%
                    time *= 10;
                    binding.circleProgress.setProgress(time);
                    binding.circleProgressVideo.setProgress(time);
                });
            }
            viewModel.getUploadModel().postValue(false);
            viewModel.getUploadVideoModel().postValue(false);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void interrupt() {
        timer = MAX_TIME;
        Toast.makeText(context, "interrupted", Toast.LENGTH_SHORT).show();
        super.interrupt();
    }
}
