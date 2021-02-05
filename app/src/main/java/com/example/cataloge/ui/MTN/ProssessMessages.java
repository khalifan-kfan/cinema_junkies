package com.example.cataloge.ui.MTN;

public class ProssessMessages {
   private final String balance_error;
   private final String Momo_excption;
    private final String Network_issue;
   private final String success;
   private boolean sent_sussessfully= false;
    public ProssessMessages(String balance_error, String momo_excption, String network_issue) {
        this.balance_error = balance_error;
        Momo_excption = momo_excption;
        Network_issue = network_issue;
        sent_sussessfully = false;
        success = null;
    }
    public ProssessMessages(boolean sent_sussessfully,String success){
        this.sent_sussessfully = sent_sussessfully;
        this.success = success;
        balance_error = null;
        Momo_excption = null;
        Network_issue = null;
    }
    public String getBalance_error() {
        return balance_error;
    }

    public String getMomo_excption() {
        return Momo_excption;
    }

    public String getNetwork_issue() {
        return Network_issue;
    }

    public boolean isSent_sussessfully() {
        return sent_sussessfully;
    }

    public String getSuccess() {
        return success;
    }
}
