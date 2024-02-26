package in.vakrangee.franchisee.networktesting.simstrength;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class MyCustomPhoneStateListener extends PhoneStateListener {

    private TelephonyManager simManager;
    private int pos;
    private IGetSignalStrength iGetSignalStrength;

    public MyCustomPhoneStateListener(TelephonyManager simManager,int pos, IGetSignalStrength iGetSignalStrength) {
        this.simManager = simManager;
        this.pos = pos;
        this.iGetSignalStrength = iGetSignalStrength;

    }

    public interface IGetSignalStrength {
        public void onSignalStrengthChanged(TelephonyManager simManager,int pos, SignalStrength signalStrength);
    }

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        iGetSignalStrength.onSignalStrengthChanged(simManager, pos, signalStrength);
    }
}
