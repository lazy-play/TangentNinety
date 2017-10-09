package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.SectionDetailContract;
import com.pudding.tangentninety.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class SectionDetailPresent extends RxPresenter<SectionDetailContract.View> implements SectionDetailContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SectionDetailPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getSectionDetail(int id) {
        mDataManager.fetchSectionDetails(id).compose(RxUtil.<SectionDetails>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SectionDetails>(mView){

                    @Override
                    public void onNext(SectionDetails sectionDetails) {
                        if(mView!=null)
                            mView.showResult(sectionDetails);
                    }
                }) ;
    }

    @Override
    public void getSectionDetailBefore(int id, long timeBefore) {
        mDataManager.fetchBeforeSectionsDetails(id,timeBefore).compose(RxUtil.<SectionDetails>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SectionDetails>(mView){

                    @Override
                    public void onNext(SectionDetails sectionDetails) {
                        if(mView!=null)
                            mView.showResult(sectionDetails);
                    }
                }) ;
    }

    @Override
    public boolean isNoPic() {
        return mDataManager.getNoImageState();
    }

}
