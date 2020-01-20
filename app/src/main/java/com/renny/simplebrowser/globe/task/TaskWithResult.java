package com.renny.simplebrowser.globe.task;

import android.os.AsyncTask;

import com.renny.simplebrowser.globe.helper.ThreadHelper;


/**
 * Created by LuckyCrystal on 2017/9/26.
 */

class TaskWithResult<Result> extends AsyncTask<Void, Integer, Result> {

    private ITaskWithResult<Result> mITaskBackground;

    public TaskWithResult(ITaskWithResult<Result> ITaskBackground) {
        mITaskBackground = ITaskBackground;
    }

    @Override
    protected Result doInBackground(Void... params) {
        Result result = null;
        if (mITaskBackground != null) {
            try {
                result = mITaskBackground.onBackground();
            } catch (final Exception e) {
                e.printStackTrace();
                if (mITaskBackground != null) {
                    ThreadHelper.postMain(new Runnable() {
                        @Override
                        public void run() {
                            mITaskBackground.onException(e);
                        }
                    });
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mITaskBackground != null) {
            mITaskBackground.onComplete(result);
        }
    }
}  