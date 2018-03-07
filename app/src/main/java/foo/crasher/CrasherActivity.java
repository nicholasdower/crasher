package foo.crasher;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.InitializationCallback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CrasherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(new Fabric.Builder(this)
                            .kits(new Crashlytics(), new CrashlyticsNdk())
                            .debuggable(true)
                            .initializationCallback(new InitializationCallback<Fabric>() {
                                @Override
                                public void success(Fabric fabric) {
                                    setContentView(R.layout.crasher);
                                }

                                @Override
                                public void failure(Exception e) {
                                }
                            })
                            .build());
    }

    public void forceCrash(View view) {
        new Crasher().crash();
    }

    public void forceNativeCrash(View view) {
        new Crasher().nativeCrash();
    }

}
