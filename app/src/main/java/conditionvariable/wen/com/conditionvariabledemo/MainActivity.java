package conditionvariable.wen.com.conditionvariabledemo;

import android.os.Bundle;
import android.os.ConditionVariable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    ConditionVariable mCV = new ConditionVariable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(1000);
                        Log.e("1111", Thread.currentThread().getId() + ",count=" + i);
                        if (i == 3) {
                            mCV.close();//重置阻塞条件,当再次运行到block()时 仍会再次阻塞； 若不重置，则不再阻塞
                            mCV.block();//当前线程阻塞，直到open()
                            Log.e("1111", "block");
                        }
                        if (i == 4) {
                            mCV.close();
                            mCV.block(3000);//当前线程阻塞，直到open() 或 超时
                            Log.e("1111", "block 3 seconds");
                            i = -1;
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @Override
    public void onClick(View view) {
        mCV.open(); //打开即唤醒 阻塞线程
    }
}
