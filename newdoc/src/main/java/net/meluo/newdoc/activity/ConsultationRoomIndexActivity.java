package net.meluo.newdoc.activity;

import android.os.Bundle;

import net.meluo.newdoc.R;
import net.meluo.newdoc.fragment.DocRoomIndexFragment;

public class ConsultationRoomIndexActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_doc_room_index);
        initView();
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        super.initView();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.adri_content, DocRoomIndexFragment.newInstance())
                .commit();
    }
}
