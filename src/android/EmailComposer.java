package org.apache.cordova.emailcomposer;

import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.text.Html;

public class EmailComposer extends CordovaPlugin {

        @Override
        public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

                if ("showEmailComposer".equals(action)) {
                                JSONObject parameters = args.getJSONObject(0);
                                if (parameters != null) {
                                        sendEmail(parameters);
                                }
                        
                        callbackContext.success();
                        return true;
                }
                return false;  // Returning false results in a "MethodNotFound" error.
        }

        private void sendEmail(JSONObject parameters) {
                
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);               

                boolean isHTML = false;
                try {
                        isHTML = parameters.getBoolean("bIsHTML");
                } catch (Exception e) {
                		isHTML = false;
                }

                if (isHTML) {
                        emailIntent.setType("text/html");
                } else {
                        emailIntent.setType("text/plain");
                }

                // setting subject
                try {
                        String subject = parameters.getString("subject");
                        if (subject != null && subject.length() > 0) {
                                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                        }
                } catch (Exception e) {
                        LOG.e("EmailComposer", "Error handling subject param: " + e.toString());
                }

                // setting body
                try {
                        String body = parameters.getString("body");
                        if (body != null && body.length() > 0) {
                        	ArrayList<String> b = new ArrayList<String>();
                        	b.add(body);
                                if (isHTML) {
                                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
                                } else {
                                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, b);
                                }
                        }
                } catch (Exception e) {
                        LOG.e("EmailComposer", "Error handling body param: " + e.toString());
                }

                // setting TO recipients
                try {
                        String toRecipients = parameters.getString("toRecipients");
                        if (toRecipients != null && toRecipients.length() > 0) {
                        	 	ArrayList<String> to = new ArrayList<String>();
                        	 	String[] toTemp = toRecipients.split(";");
                        	 	for(int i = 0; i<toTemp.length; i++){
                        	 		to.add(toTemp[i]);
                        	 	}
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, to);
                        }
                } catch (Exception e) {
                        LOG.e("EmailComposer", "Error handling toRecipients param: " + e.toString());
                }

                // setting CC recipients
                try {
                	String ccRecipients = parameters.getString("ccRecipients");
                        if (ccRecipients != null && ccRecipients.length() > 0) {
                        		ArrayList<String> cc = new ArrayList<String>();
                        	 	String[] ccTemp = ccRecipients.split(";");
                        	 	for(int i = 0; i<ccTemp.length; i++){
                        	 		cc.add(ccTemp[i]);
                        	 	}
                                emailIntent.putExtra(android.content.Intent.EXTRA_CC, cc);
                        }
                } catch (Exception e) {
                        LOG.e("EmailComposer", "Error handling ccRecipients param: " + e.toString());
                }

                // setting BCC recipients
                try {
                	String bccRecipients = parameters.getString("bccRecipients");
                        if (bccRecipients != null && bccRecipients.length() > 0) {
                                ArrayList<String> bcc = new ArrayList<String>();
                        	 	String[] bccTemp = bccRecipients.split(";");
                        	 	for(int i = 0; i<bccTemp.length; i++){
                        	 		bcc.add(bccTemp[i]);
                        	 	}
                                emailIntent.putExtra(android.content.Intent.EXTRA_BCC, bcc);
                        }
                } catch (Exception e) {
                        LOG.e("EmailComposer", "Error handling bccRecipients param: " + e.toString());
                }
                
                 this.cordova.getActivity().startActivity(emailIntent);
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
                super.onActivityResult(requestCode, resultCode, intent);
                LOG.e("EmailComposer", "ResultCode: " + resultCode);
                // IT DOESN'T SEEM TO HANDLE RESULT CODES
        }
}