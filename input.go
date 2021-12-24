package main

import (
	"regexp"
	"strconv"
)

var digits = regexp.MustCompile("-?\\d+")

// read a string of integers separated by any non-digit
// scanner provided should scan by lines
func readInts(text string) []int64 {
	result := make([]int64, 0, 1000)
	for _, s := range digits.FindAllString(text, -1) {
		i, _ := strconv.ParseInt(s, 0, 0)
		result = append(result, i)
	}
	return result
}
