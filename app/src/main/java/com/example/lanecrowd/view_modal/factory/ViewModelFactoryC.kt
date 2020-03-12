package com.example.lanecrowd.view_modal.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lanecrowd.modal.repository.UserRepository
import com.example.lanecrowd.view_modal.CommentVM
import com.example.lanecrowd.view_modal.FetchPostVm
import com.example.lanecrowd.view_modal.LoginRegUserVM
import com.example.lanecrowd.view_modal.Profile_VM

@Suppress("UNCHECKED_CAST")
class ViewModelFactoryC(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass == FetchPostVm::class.java) {
            return FetchPostVm(repository) as T
        }
        else if (modelClass == LoginRegUserVM::class.java) {

            return LoginRegUserVM(repository) as T
        }
        else if (modelClass == CommentVM::class.java) {

            return CommentVM(repository) as T
        }else if (modelClass == CommentVM::class.java) {

            return CommentVM(repository) as T
        }else if (modelClass == Profile_VM::class.java) {

            return Profile_VM(repository) as T
        }

        throw IllegalArgumentException("Unknown_class_name")


    }


}