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
	return sumSlice(result)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	ages := readInts(scanner.Text())
	count := make([]int64, 9)
	for _, age := range ages {
		count[age]++
	}
	fmt.Println(simulate(count, 80))
	fmt.Println(simulate(count, 256))
}
