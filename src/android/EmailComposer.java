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
		if(parameters.has("bIsHTML")){
			try {
				isHTML = parameters.getBoolean("bIsHTML");
			} catch (Exception e) {
				isHTML = false;
			}
		}
		if (isHTML) {
			emailIntent.setType("text/html");
		} else {
			emailIntent.setType("text/plain");
		}


		// setting subject
		if(parameters.has("subject")){
			try {
				String subject = parameters.getString("subject");
				if (subject != null && subject.length() > 0) {
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
				}
			} catch (Exception e) {
				LOG.e("EmailComposer", "Error handling subject param: " + e.toString());
			}
		}

		// setting body

		if(parameters.has("body")){
			String body;
			try {
				body = parameters.getString("body");
				if (body != null && body.length() > 0) {
					if (isHTML) {
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
					} else {
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		// setting TO recipients
		if(parameters.has("toRecipients")){
			String toRecipients;
			try {
				toRecipients = parameters.getString("toRecipients");
				if (toRecipients != null && toRecipients.length() > 0) {
					String [] to = toRecipients.split(";");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, to);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		// setting CC recipients
		if(parameters.has("ccRecipients")){
			String ccRecipients;
			try {
				ccRecipients = parameters.getString("ccRecipients");
				if (ccRecipients != null && ccRecipients.length() > 0) {
					String [] cc = ccRecipients.split(";");
					emailIntent.putExtra(android.content.Intent.EXTRA_CC, cc);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// setting BCC recipients
		if(parameters.has("bccRecipients")){
			String bccRecipients;
			try {
				bccRecipients = parameters.getString("bccRecipients");
				if (bccRecipients != null && bccRecipients.length() > 0) {
					String [] bcc = bccRecipients.split(";");
					emailIntent.putExtra(android.content.Intent.EXTRA_BCC, bcc);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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