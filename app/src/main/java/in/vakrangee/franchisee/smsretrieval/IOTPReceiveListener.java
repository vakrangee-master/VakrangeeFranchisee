package in.vakrangee.franchisee.smsretrieval;

public interface IOTPReceiveListener {

    public void onOTPReceived(String otp);

    public void onOTPTimeOut();

    public void onOTPReceivedError(String error);
}
