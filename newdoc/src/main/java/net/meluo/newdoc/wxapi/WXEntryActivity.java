package net.meluo.newdoc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.way.util.T;
import com.ypy.eventbus.EventBus;

import net.meluo.newdoc.R;
import net.meluo.newdoc.app.NDApp;
import net.meluo.newdoc.app.NDEvent;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private IWXAPI api = NDApp.getInstance().getWXApi();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_me_index);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setContentView(R.layout.f_login);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:

                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp re = (SendAuth.Resp) resp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    NDApp.getInstance().WXCode = re.code;
                    EventBus.getDefault().post(new NDEvent(NDEvent.E_WX_LOGIN));
                    this.finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.errcode_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.errcode_deny;
                    break;
                default:
                    result = R.string.errcode_unknown;
                    break;
            }

        }
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {

            // SendAuth.Resp re = (SendAuth.Resp) resp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    // NDApp.getInstance().WXCode = re.code;
                    // EventBus.getDefault().post(new NDEvent(NDEvent.E_WX_LOGIN));
                    this.finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.errcode_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.errcode_deny;
                    break;
                default:
                    result = R.string.errcode_unknown;
                    break;
            }

        }

    }
}