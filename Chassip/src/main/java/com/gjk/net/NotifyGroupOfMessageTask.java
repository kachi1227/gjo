package com.gjk.net;

import android.content.Context;

import com.gjk.net.MiluHttpRequest.DBHttpResponse;

import org.json.JSONObject;

public class NotifyGroupOfMessageTask extends MiluHTTPTask {

    private long mGroupId;

    public NotifyGroupOfMessageTask(Context ctx, HTTPTaskListener listener, long groupId) {
        super(ctx, listener);
        mGroupId = groupId;
        execute();
    }

    @Override
    public TaskResult handleSuccessfulJSONResponse(DBHttpResponse response, JSONObject json) throws Exception {
        return new TaskResult(this, TaskResult.RC_SUCCESS, null, json);
    }

    @Override
    public JSONObject getPayload() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("group_id", mGroupId);
        return payload;
    }

    @Override
    public String getUri() {
        return "api/notifyGroupOfMessage";
    }

}