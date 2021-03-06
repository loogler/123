/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
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
 *******************************************************************************/
package com.qingyuan.modem.photo;

import android.view.MotionEvent;
/**
 *   接口类  手势判断
 *   
 * @author Administrator
 *	@method  boolean onTouchEvent(MotionEvent ev) 
 *	@method	 boolean isScaling()
 *	@method	 void setOnGestureListener(OnGestureListener listener)
 */
public interface Util_GestureDetector {

    public boolean onTouchEvent(MotionEvent ev);

    public boolean isScaling();

    public void setOnGestureListener(OnGestureListener listener);

}
