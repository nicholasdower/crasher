package foo.crasher;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.InitializationCallback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.CountDownLatch;

public class CrasherActivity extends AppCompatActivity {

    private int threadCount = 0;
    private Crasher crasher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crasher = new Crasher();
        Fabric.with(new Fabric.Builder(this)
                            .kits(new Crashlytics(), new CrashlyticsNdk())
                            .debuggable(true)
                            .initializationCallback(new InitializationCallback<Fabric>() {
                                @Override
                                public void success(Fabric fabric) {
                                    setContentView(R.layout.crasher);
                                    setThreadCount();
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
        threadCount += 10;
        setThreadCount();
    }

    public void forceCrashOnMainThread(View view) {
        disableButtons();
        crasher.crash();
    }

    public void forceCrashOnBackgroundThread(View view) {
        disableButtons();
        new Thread(new Runnable() {
            @Override
            public void run() {
                crasher.crash();
            }
        }).start();
    }

    public void forceNativeCrashOnMainThread(View view) {
        disableButtons();
        crasher.nativeCrash();
    }

    public void forceNativeCrashOnBackgroundThread(View view) {
        disableButtons();
        new Thread(new Runnable() {
            @Override
            public void run() {
                crasher.nativeCrash();
            }
        }).start();
    }

    private void setThreadCount() {
        TextView threadCountTextView = findViewById(R.id.thread_count);
        threadCountTextView.setText(getString(R.string.thread_count, threadCount));
    }

    private void disableButtons() {
        findViewById(R.id.start_threads).setEnabled(false);
        findViewById(R.id.force_crash_on_background_thread).setEnabled(false);
        findViewById(R.id.force_crash_on_main_thread).setEnabled(false);
        findViewById(R.id.force_native_crash_on_background_thread).setEnabled(false);
        findViewById(R.id.force_native_crash_on_main_thread).setEnabled(false);
    }
}
