package com.gjk.net;

import android.content.Context;

import com.gjk.net.MiluHttpRequest.DBHttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotifyWhisperInviteesTask extends MiluHTTPTask {

    private long mId;
    private long mWhisperId;
    private long[] mRecipients;

    public NotifyWhisperInviteesTask(Context ctx, HTTPTaskListener listener, long id, long whisperId, long[] recipients) {
        super(ctx, listener);
        mId = id;
        mWhisperId = whisperId;
        mRecipients = recipients;
        execute();
    }

    @Override
    public TaskResult handleSuccessfulJSONResponse(DBHttpResponse response, JSONObject json) throws Exception {
        return new TaskResult(this, TaskResult.RC_SUCCESS, null, json);
    }

    @Override
    public JSONObject getPayload() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("id", mId);
        payload.put("whisper_id", mWhisperId);
        JSONArray ids = new JSONArray();
        for (long id : mRecipients) {
            ids.put(id);
        }
        payload.put("recipients", ids);
        return payload;
    }

    @Override
    public String getUri() {
        return "api/notifyWhisperInvitees";
    }

}