package com.happycoderz.cryptfolio.jobs;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class CoinJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case CoinIntervalJob.TAG:
                return new CoinIntervalJob();
            default:
                return null;
        }
    }
}