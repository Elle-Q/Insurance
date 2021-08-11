package com.fintech.insurance.commons.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.*;

/**
 * 算法帮助类
 *
 * @author qxy
 * @since 2017-12-26
 * @version 1.0.0
 */
public class ArithmeticUtils {

    // 用于存放哪些被使用了
    private static LinkedList<ArithmeticVO> selected = new LinkedList<>();
    // 最大值
    private static Long resultAmount = 0L;

    private static void init() {
        selected.clear();
        resultAmount = 0L;
    };

    /**
     * 添加同步锁
     * @param maxAmount
     * @param arithmeticVOS
     * @return
     */
    public static synchronized List<ArithmeticVO> getDebtContractArray(long maxAmount, List<ArithmeticVO> arithmeticVOS) {
        if (arithmeticVOS == null || arithmeticVOS.size() <= 0) {
            return Collections.EMPTY_LIST;
        }
        init();

        // 最终结果
        List<ArithmeticVO> resultArray = new ArrayList<>();
        try {
            dp(maxAmount, 0, arithmeticVOS, -1, resultArray);
        } catch (Exception e) {
        } finally {
            init();
        }
        return resultArray;
    }

    /**
     * 从数组抽出一些数，尽可能的接近maxAmount，且不大于maxAmount
     * index, 存放上一层递归下表索引
     * @return
     */
    private static void dp(long maxAmount, long currentAmount, List<ArithmeticVO> arithmeticVOS, int index, List<ArithmeticVO> resultArray) {
        if (maxAmount < currentAmount) { // 结束递归
            ArithmeticVO temp = selected.pollLast();
            Long tempResult = 0L;
            for (ArithmeticVO element : selected) {
                tempResult += element.getNumber();
            }
            if (tempResult > resultAmount) {
                resultAmount = tempResult;
                resultArray.clear();
                resultArray.addAll(selected);
            }
            selected.add(temp);
        } else {
            for (int i = index+1; i < arithmeticVOS.size(); i++) {
                selected.add(arithmeticVOS.get(i));
                count++;
                dp(maxAmount, currentAmount + arithmeticVOS.get(i).getNumber(), arithmeticVOS, i, resultArray);
                selected.pollLast();
            }
        }
    }

    private static int count = 0;
    public static List<ArithmeticVO> dpNew(Long maxAmount, List<ArithmeticVO> arithmeticVOS, int deepth) {
        if (deepth > 1000) {
            return Collections.EMPTY_LIST;
        }
        for (ArithmeticVO model : arithmeticVOS) {
            if (maxAmount == model.getNumber()) {
                List<ArithmeticVO> result = new ArrayList<>();
                result.add(model);
                return result;
            }
        }

        //当总金额都比最大限额更小时， 直接返回所有元素
        Long totalAmount = 0L;
        for (ArithmeticVO model : arithmeticVOS) {
            totalAmount += model.getNumber();
        }
        if (totalAmount <= maxAmount) {
            return arithmeticVOS;
        }

        // 删除超过maxAmout的节点
        Iterator<ArithmeticVO> voIterator = arithmeticVOS.iterator();
        while (voIterator.hasNext()) {
            ArithmeticVO model = voIterator.next();
            if (maxAmount < model.getNumber()) {
                voIterator.remove();
            }
        }

        if (arithmeticVOS.size() == 1) {
            return arithmeticVOS;
        }

        // 从小到大排序
        Collections.sort(arithmeticVOS, new Comparator<ArithmeticVO>() {
            @Override
            public int compare(ArithmeticVO o1, ArithmeticVO o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        List<List<ArithmeticVO>> candidateResult = new ArrayList<>();

        for (int i = 0; i < arithmeticVOS.size(); i++) {
            List<ArithmeticVO> candidate = new ArrayList<>();
            candidate.add(arithmeticVOS.get(i));
            if (i < (arithmeticVOS.size() - 1)) {
                List<ArithmeticVO> leavingData = new ArrayList<>();
                for (int j = i; j < arithmeticVOS.size(); j++) {

                    if (j != i) {
                        leavingData.add(arithmeticVOS.get(j));
                    }
                }
                candidate.addAll(ArithmeticUtils.dpNew(maxAmount - arithmeticVOS.get(i).getNumber(), leavingData, deepth+1));
            }
            candidateResult.add(candidate);
        }

        // 比较所有候选组合， 返回最优的结果
        // 候选结果与最大值的距离 = maxAmount - 合并值
        int minDistanceIndex = -1;
        Long minDistanceValue = Long.MAX_VALUE;

        for (int index = 0; index < candidateResult.size(); index ++ ) {
            List<ArithmeticVO> candiateOne = candidateResult.get(index);
            Long combineAmount = 0L;
            for (ArithmeticVO vo : candiateOne) {
                combineAmount += vo.getNumber();
            }
            Long distance = 0L;
            if (combineAmount > maxAmount) {
                distance = Long.MAX_VALUE;
            } else {
                distance = maxAmount - combineAmount;
            }
            if (7 == arithmeticVOS.size()) {
                System.out.println(" " + index + "  distance = " + distance);
            }

            if (distance < minDistanceValue) {
                minDistanceValue = distance;
                minDistanceIndex = index;
            }
        }

        //打印debug信息
        if (7 == arithmeticVOS.size()) {
            for (int i = 0; i < candidateResult.size(); i++) {
                System.out.println("==========Candidate result: " + i);
                List<ArithmeticVO> candiateOne = candidateResult.get(i);
                for (ArithmeticVO vo : candiateOne) {
                    System.out.println(vo);
                }
            }
            System.out.println("==========Candidate result index:" + minDistanceIndex);
        }

        return minDistanceIndex == -1 ? Collections.EMPTY_LIST : candidateResult.get(minDistanceIndex);
    }

    public static void main(String[] args) {
        List<ArithmeticVO> arithmeticVOS = new ArrayList<>();
        arithmeticVOS.add(new ArithmeticVO(11413, 70310L));
        arithmeticVOS.add(new ArithmeticVO(2, 60103L));
        arithmeticVOS.add(new ArithmeticVO(4, 109198L));
        arithmeticVOS.add(new ArithmeticVO(5, 66157L));
       /* arithmeticVOS.add(new ArithmeticVO(6, 30L));
        arithmeticVOS.add(new ArithmeticVO(7, 40L));*/



        List<ArithmeticVO> result = ArithmeticUtils.dpNew(100000l, arithmeticVOS, 0);
        System.out.println("+++++++++++++ Final result :" + result.size());
        for (ArithmeticVO vo : result) {
            System.out.println(vo.getNumber());
        }
        System.out.println("\n\n\n" + count);
    }


}
