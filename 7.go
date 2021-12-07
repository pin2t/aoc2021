package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	digits := regexp.MustCompile("\\d+")
	pos := make([]int, 0, 1000)
	maxpos := 0
	for _, s := range digits.FindAllString(scanner.Text(), -1) {
		p, _ := strconv.ParseInt(s, 0, 0)
		pos = append(pos, int(p))
		if int(p) > maxpos {
			maxpos = int(p)
		}
	}
	result := 10000000000
	result2 := 10000000000
	for p := 0; p <= maxpos; p++ {
		sum, sum2 := 0, 0
		for _, i := range pos {
			if i > p {
				sum += i - p
				for j := p; j <= i; j++ {
					sum2 += (j - p)
				}
			} else {
				sum += p - i
				for j := i; j <= p; j++ {
					sum2 += (j - i)
				}
			}
		}
		if sum < result {
			result = sum
		}
		if sum2 < result2 {
			result2 = sum2
		}
	}
	fmt.Println(result, result2)
}
