package io.github.mohamedisoliman.mvi.mymvi;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

/**
 * Created by Mohamed Ibrahim on 9/27/18.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MviLoginView mParam;

    public ViewModelFactory(MviLoginView param) {
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LoginViewModel(mParam);
    }
}
