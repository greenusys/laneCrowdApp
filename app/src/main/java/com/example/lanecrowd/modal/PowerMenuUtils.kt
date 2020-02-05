/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lanecrowd.modal

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity

import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.lanecrowd.R

import com.skydoves.powermenu.CircularEffect
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.OnMenuItemClickListener
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem

object PowerMenuUtils {

    fun getFriendOptionMenu(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem>,
        onDismissedListener: () -> Int
    ): PowerMenu {


        return PowerMenu.Builder(context)
            .addItem(PowerMenuItem(context.getString(R.string.block), false))
            .addItem(PowerMenuItem(context.getString(R.string.unfriend), false))
            .setAutoDismiss(true)
            .setLifecycleOwner(lifecycleOwner)
            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
            .setCircularEffect(CircularEffect.BODY)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
            .setTextSize(12)
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
            .setSelectedTextColor(Color.WHITE)
            .setMenuColor(Color.WHITE)
            .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .setPreferenceName("HamburgerPowerMenu")
            .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
            .build()
    }


    fun getSelfPostMenu(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem>,
        onDismissedListener: () -> Int
    ): PowerMenu {


        return PowerMenu.Builder(context)
            .addItem(PowerMenuItem(context.getString(R.string.edit), false))
            .addItem(PowerMenuItem(context.getString(R.string.delete), false))
            .addItem(PowerMenuItem(context.getString(R.string.share), false))
            .setAutoDismiss(true)
            .setLifecycleOwner(lifecycleOwner)
            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
            .setCircularEffect(CircularEffect.BODY)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
            .setTextSize(12)
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
            .setSelectedTextColor(Color.WHITE)
            .setMenuColor(Color.WHITE)
            .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .setPreferenceName("HamburgerPowerMenu")
            .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
            .build()
    }



 fun getCommentMenu(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem>,
        onDismissedListener: () -> Int
    ): PowerMenu {


        return PowerMenu.Builder(context)
            .addItem(PowerMenuItem(context.getString(R.string.edit), false))
            .addItem(PowerMenuItem(context.getString(R.string.delete), false))
            .setAutoDismiss(true)
            .setLifecycleOwner(lifecycleOwner)
            .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
            .setCircularEffect(CircularEffect.BODY)
            .setMenuRadius(10f)
            .setMenuShadow(10f)
            .setTextColor(ContextCompat.getColor(context, R.color.black))
            .setTextSize(12)
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
            .setSelectedTextColor(Color.WHITE)
            .setMenuColor(Color.WHITE)
            .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .setPreferenceName("HamburgerPowerMenu")
            .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
            .build()
    }




}
