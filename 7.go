package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	pos := readInts(scanner.Text())
	maxpos := maxSlice(pos)
	fuel := make([]int64, 0, maxpos+1)
	fuel2 := make([]int64, 0, maxpos+1)
	for p := int64(0); p <= maxpos; p++ {
		sum, sum2 := int64(0), int64(0)
		for _, i := range pos {
			sum += abs(i - p)
			sum2 += (abs(i-p) + 1) * (i + p - 2*min(i, p)) / 2
		}
		fuel = append(fuel, sum)
		fuel2 = append(fuel2, sum2)
	}
	fmt.Println(minSlice(fuel), minSlice(fuel2))
}
