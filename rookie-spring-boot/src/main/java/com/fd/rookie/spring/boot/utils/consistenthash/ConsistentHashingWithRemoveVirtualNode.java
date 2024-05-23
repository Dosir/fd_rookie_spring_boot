package com.fd.rookie.spring.boot.utils.consistenthash;

import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author fd
 * @Description 带虚拟节点的一致性hash实现-删除节点
 * @createTime 2024-03-28 11:42
 **/
public class ConsistentHashingWithRemoveVirtualNode {
    /**
     * 集群地址列表
     */
    private static String[] groups = {
            "192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
            "192.168.0.3:111", "192.168.0.4:111"
    };

    /**
     * 真实集群列表
     */
    private static List<String> realGroups = new LinkedList<>();

    /**
     * 虚拟节点映射关系
     * key: hash值
     * value: 虚拟节点名称
     */
    private static SortedMap<Integer, String> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点的数量
     */
    private static final int VIRTUAL_NODE_NUM = 1000;

    static {
        // 先添加真实节点列表
        realGroups.addAll(Arrays.asList(groups));

        // 将虚拟节点映射到Hash环上
        for (String realGroup: realGroups) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                String virtualNodeName = getVirtualNodeName(realGroup, i);
                int hash = HashUtil.getHash(virtualNodeName);
//                System.out.println("[" + virtualNodeName + "] launched @ " + hash);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }

    /**
     * 刷新虚拟节点的hash环
     */
    private static void refreshHashCircle() {
        // 当集群变动时，刷新hash环，其余的集群在hash环上的位置不会发生变动
        virtualNodes.clear();
        for (String realGroup: realGroups) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                String virtualNodeName = getVirtualNodeName(realGroup, i);
                int hash = HashUtil.getHash(virtualNodeName);
//                System.out.println("[" + virtualNodeName + "] launched @ " + hash);
                virtualNodes.put(hash, virtualNodeName);
            }
        }
    }


    /**
     * 删除节点
     *
     * @param identifier 真实的节点地址
     */
    private static void removeGroup(String identifier) {
        Iterator it = realGroups.iterator();
        while (it.hasNext()) {
            if (it.next().equals(identifier)) {
                it.remove();
            }
        }
        refreshHashCircle();
    }


    /**
     * 这里真实节点和虚拟节点的映射采用了字符串拼接的方式，
     * 这种方式虽然简单但很有效，memcached官方也是这么实现的。
     *
     * @param realName
     * @param num
     * @return
     */
    private static String getVirtualNodeName(String realName, int num) {
        return realName + "&&VN" + num;
    }

    private static String getRealNodeName(String virtualName) {
        return virtualName.split("&&")[0];
    }

    private static String getServer(String widgetKey) {
        int hash = HashUtil.getHash(widgetKey);
        // 只取出所有大于该hash值的部分而不必遍历整个Tree
        SortedMap<Integer, String> subMap = virtualNodes.tailMap(hash);
        String virtualNodeName;
        if (subMap == null || subMap.isEmpty()) {
            // hash值在最尾部，应该映射到第一个group上
            virtualNodeName = virtualNodes.get(virtualNodes.firstKey());
        }else {
            virtualNodeName = subMap.get(subMap.firstKey());
        }
        return getRealNodeName(virtualNodeName);
    }

    public static void main(String[] args) {
        // 生成随机数进行测试
        Map<String, Set<Integer>> resMap = new HashMap<>();

        for (int i = 0; i < 1000; i++) {
            Integer widgetId = i;
            String group = getServer(widgetId.toString());
            if (resMap.containsKey(group)) {
                resMap.get(group).add(widgetId);
                resMap.put(group, resMap.get(group));
            } else {
                resMap.put(group, Sets.newHashSet(widgetId));
            }
        }

        resMap.forEach(
                (k, v) -> {
                    System.out.println("group " + k + ": " + v + "(" + v.size()/1000.0D +"%)");
                }
        );

        System.out.println("=============删除节点===============");
        removeGroup("192.168.0.0:111");
        refreshHashCircle();

        // 生成随机数进行测试
//        Map<String, Set<Integer>> removedMap = new HashMap<>();
//        for (int i = 0; i < 1000; i++) {
//            Integer widgetId = i;
//            String group = getServer(widgetId.toString());
//            if (removedMap.containsKey(group)) {
//                removedMap.get(group).add(widgetId);
//                removedMap.put(group, removedMap.get(group));
//            } else {
//                removedMap.put(group, Sets.newHashSet(widgetId));
//            }
//        }

//        removedMap.forEach(
//                (k, v) -> {
//                    System.out.println("group " + k + ": " + v + "(" + v.size()/1000.0D +"%)");
//                }
//        );

        System.out.println("=============变更了位置的数================");
        Map<String, Set<Integer>> notExistMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            Integer widgetId = i;
            String group = getServer(widgetId.toString());
            if (!resMap.get(group).contains(widgetId)) {
                // 如果数不在原来的数值上
                if (notExistMap.containsKey(group)) {
                    notExistMap.get(group).add(widgetId);
                    notExistMap.put(group, notExistMap.get(group));
                } else {
                    notExistMap.put(group, Sets.newHashSet(widgetId));
                }
            }
        }

        notExistMap.forEach(
                (k, v) -> {
                    System.out.println("group " + k + ": " + v);
                }
        );
    }


}
