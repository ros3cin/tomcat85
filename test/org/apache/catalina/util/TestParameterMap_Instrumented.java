/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.catalina.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.EnergyCheckUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestParameterMap_Instrumented {

    @Test
    public void stressParameterMap() {
        ParameterMap paramMap = new ParameterMap<>();
        int sampleSize = 30;
        int effectiveSize = sampleSize*2;
        double[] values = new double[sampleSize];
        EnergyCheckUtils.ProfileInit();
        for(int i = 0; i < effectiveSize; i++){
            double statsBefore = EnergyCheckUtils.getEnergyStats()[2];
            for (int j = 0; j < 10000000; j++) {
                paramMap.put(j+i, j+i);
            }
            double statsAfter = EnergyCheckUtils.getEnergyStats()[2];
            if(i > sampleSize) {//effectiveSize/2
                values[i-sampleSize-1]=statsAfter-statsBefore;
            }
            System.out.printf("Consumption:%f\n",statsAfter-statsBefore);
            paramMap.clear();
        }



        org.EnergyCheckUtils.ProfileDealloc();
        for(int i = 0; i < sampleSize;i++){
            System.out.print(values[i]+",");
        }
        System.out.println();
    }
}