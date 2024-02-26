package in.vakrangee.franchisee.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.vakrangee.franchisee.R;
import in.vakrangee.franchisee.sitelayout.NextGenPhotoViewPager;

public class NextGenAcitivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_gen_acitivity);

        Button msubmitRecharge = (Button) findViewById(R.id.msubmitRecharge);
        msubmitRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NextGenAcitivity.this, NextGenPhotoViewPager.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
}
