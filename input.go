package main

import (
	"bufio"
	"regexp"
	"strconv"
)

var digits = regexp.MustCompile("\\d+")

// read a string of integers separated by any non-digit
// scanner provided should scan by lines
func readInts(scanner *bufio.Scanner) []int64 {
	scanner.Scan()
	result := make([]int64, 0, 1000)
	for _, s := range digits.FindAllString(scanner.Text(), -1) {
		i, _ := strconv.ParseInt(s, 0, 0)
		result = append(result, i)
	}
	return result
}
