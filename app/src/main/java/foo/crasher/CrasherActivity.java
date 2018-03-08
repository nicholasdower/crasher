package foo.crasher;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.InitializationCallback;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class CrasherActivity extends AppCompatActivity {

    private Crasher crasher;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crasher = new Crasher();
        handler = new Handler();
        Fabric.with(new Fabric.Builder(this)
                            .kits(new Crashlytics(), new CrashlyticsNdk())
                            .debuggable(true)
                            .initializationCallback(new InitializationCallback<Fabric>() {
                                @Override
                                public void success(Fabric fabric) {
                                    setContentView(R.layout.crasher);
                                    startThreadCounter();
                                }

                                @Override
                                public void failure(Exception e) {
                                }
                            })
                            .build());
    }

    public void startThreads(View view) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new CountDownLatch(1).await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void forceCrashOnMainThread(View view) {
        disableButtonsAndStartStopwatch();
        crasher.crash();
    }

    public void forceCrashOnBackgroundThread(View view) {
        disableButtonsAndStartStopwatch();
        new Thread(new Runnable() {
            @Override
            public void run() {
                crasher.crash();
            }
        }).start();
    }

    public void forceNativeCrashOnMainThread(View view) {
        disableButtonsAndStartStopwatch();
        crasher.nativeCrash();
    }

    public void forceNativeCrashOnBackgroundThread(View view) {
        disableButtonsAndStartStopwatch();
        new Thread(new Runnable() {
            @Override
            public void run() {
                crasher.nativeCrash();
            }
        }).start();
    }

    private void disableButtonsAndStartStopwatch() {
        findViewById(R.id.start_threads).setEnabled(false);
        findViewById(R.id.force_crash_on_background_thread).setEnabled(false);
        findViewById(R.id.force_crash_on_main_thread).setEnabled(false);
        findViewById(R.id.force_native_crash_on_background_thread).setEnabled(false);
        findViewById(R.id.force_native_crash_on_main_thread).setEnabled(false);

        final TextView timerView = findViewById(R.id.stopwatch);
        final long start = System.nanoTime() / 1000000000;
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long totalSeconds = (System.nanoTime() / 1000000000) - start;
                timerView.setText(String.format(Locale.US, "%02d:%02d", totalSeconds / 60, totalSeconds % 60));
                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }

    private void startThreadCounter() {
        final TextView threadCountTextView = findViewById(R.id.thread_count);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int threadCount = Thread.getAllStackTraces().size();
                threadCountTextView.setText(getString(R.string.thread_count, threadCount));
                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }
}
