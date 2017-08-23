/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.jing.cloud.service.aligorithm;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ModuloShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> ,SingleKeyTableShardingAlgorithm<Long>{

    //分片数量 ，这个是否可以通过 方法中的dataSourceNames.size来取
    private int sharding;

    private boolean endWith(String str,long value){
        String v = String.format("_%02d",value%sharding);
        return str.endsWith(v);
    }


    @Override
    public String doEqualSharding(final Collection<String> names, final ShardingValue<Long> shardingValue) {
        for (String each : names) {
            if (endWith(each,shardingValue.getValue())) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public Collection<String> doInSharding(final Collection<String> names, final ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(names.size());
        for (Long value : shardingValue.getValues()) {
            for (String name : names) {
                if (endWith(name,value)) {
                    result.add(name);
                    //如果已经是全表扫描了，那么直接返回
                    if(result.size()==names.size()){
                        return names;
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    public Collection<String> doBetweenSharding(final Collection<String> names, final ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(names.size());
        Range<Long> range = shardingValue.getValueRange();

        if(range.upperEndpoint()-range.lowerEndpoint()+1>=names.size()){
            return names;
        }
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : names) {
                if (endWith(each,i)) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
