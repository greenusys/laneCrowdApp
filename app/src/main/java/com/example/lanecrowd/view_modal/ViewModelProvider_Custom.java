package com.example.lanecrowd.view_modal;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ViewModelProvider_Custom extends ViewModelProvider.NewInstanceFactory {
    private MySessionVM myViewModel;

    private final Map<Class<? extends ViewModel>, ViewModel> mFactory = new HashMap<>();

    public ViewModelProvider_Custom(MySessionVM myViewModel) {
        this.myViewModel = myViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(final @NonNull Class<T> modelClass) {
        mFactory.put(modelClass, myViewModel);

        if (MySessionVM.class.isAssignableFrom(modelClass)) {
            MySessionVM shareVM = null;

            if (mFactory.containsKey(modelClass)) {
                shareVM = (MySessionVM) mFactory.get(modelClass);
            } else {
                try {
                    shareVM = (MySessionVM) modelClass.getConstructor(Runnable.class).newInstance(new Runnable() {
                        @Override
                        public void run() {
                            mFactory.remove(modelClass);
                        }
                    });
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Cannot create an instance of " + modelClass, e);
                }
                mFactory.put(modelClass, shareVM);
            }

            return (T) shareVM;
        }
        return super.create(modelClass);
    }

}