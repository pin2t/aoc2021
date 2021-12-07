package main

import (
	"bufio"
	"fmt"
	"os"
)

func simulate(count []int64, days int) int64 {
	result := make([]int64, 9)
	copy(result, count)
	prev := make([]int64, 9)
	for day := 1; day <= days; day++ {
		copy(prev, result)
		result = make([]int64, 9)
		result[6] += prev[0]
		result[8] += prev[0]
		for i := 1; i < len(prev); i++ {
			result[i-1] += prev[i]
		}
	}
	sum := int64(0)
	for _, c := range result {
		sum += c
	}
	return sum
}

func main() {
	ages := readInts(bufio.NewScanner(os.Stdin))
	count := make([]int64, 9)
	for _, age := range ages {
		count[age]++
	}
	fmt.Println(simulate(count, 80))
	fmt.Println(simulate(count, 256))
}
