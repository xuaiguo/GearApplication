package gear.yc.com.gearapplication.ui.mvp.travelnotes;

import gear.yc.com.gearapplication.network.APIServiceManager;
import gear.yc.com.gearapplication.network.helper.SchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;

/**
 * GearApplication
 * Created by YichenZ on 2016/7/4 15:56.
 */

public class TravelNotesPresenter implements TravelNotesContract.Presenter {
    private TravelNotesContract.View view;
    public TravelNotesActivity obj;

    public TravelNotesPresenter(TravelNotesContract.View view, TravelNotesActivity obj) {
        this.view = view;
        this.obj = obj;
    }

    @Override
    public int refreshData(String key,int page) {
        page = 1;
        loadData(key,page);
        return page;
    }

    @Override
    public void loadData(String key,int page) {
        view.showDialog();
        APIServiceManager.getInstance()
                .getTravelNotesAPI()
                .getTravelNotesList(key, page + "")
                .compose(obj.bindToLifecycle())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> view.disDialog())
                .subscribe(s -> {
                            RxBus.getInstance().post(RxBus.TAG_DEFAULT, s.getBookses());
                        },
                        e -> {
                            RxBus.getInstance().post(RxBus.TAG_ERROR, e.getMessage());
                        });
    }

    @Override
    public void start() {
    }
}