package cqf.hn.pastedata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cqf.hn.pastedata.lib.PasteData;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.tv)
    TextView tv;
    private DstData mData;
    private SrcData1 mSrcData1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //EventBus.getDefault().register(this);
        //ButterKnife.bind(this);
        findViewById(R.id.tv).setOnClickListener(this);
        mData = new DstData();
        mSrcData1 = new SrcData1();
        mSrcData1.setId("1");
        mSrcData1.setTitle("标题");
        mSrcData1.setDesc("描述");
        mSrcData1.setType1("type_1");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv:
                PasteData.getInstance().paste(mData, mSrcData1);
                Log.d("shan", mData.toString());
                break;
        }
    }
}
