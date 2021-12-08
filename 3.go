package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func zeros(values []string, bit int) int {
	var zeros int
	for _, v := range values {
		if v[bit] == '0' {
			zeros++
		}
	}
	return zeros
}

func filter(values []string, bit int, bitval int) []string {
	result := make([]string, 0, 1000)
	for _, v := range values {
		if v[bit] == byte(bitval)+48 {
			result = append(result, v)
		}
	}
	return result
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	values := make([]string, 0, 1000)
	for scanner.Scan() {
		values = append(values, scanner.Text())
	}
	var gamma, epsilon int
	for i := 0; i < len(values[0]); i++ {
		zeros := zeros(values, i)
		if zeros > len(values)-zeros {
			gamma = gamma*2 + 0
			epsilon = epsilon*2 + 1
		} else {
			gamma = gamma*2 + 1
			epsilon = epsilon*2 + 0
		}
	}
	fmt.Println(gamma * epsilon)
	left := values
	for bit := 0; bit < len(values[0]) && len(left) > 1; bit++ {
		zeros := zeros(left, bit)
		if zeros > len(left)-zeros {
			left = filter(left, bit, 0)
		} else {
			left = filter(left, bit, 1)
		}
	}
	oxygen, _ := strconv.ParseInt(left[0], 2, 32)
	left = values
	for bit := 0; bit < len(values[0]) && len(left) > 1; bit++ {
		zeros := zeros(left, bit)
		if zeros > len(left)-zeros {
			left = filter(left, bit, 1)
		} else {
			left = filter(left, bit, 0)
		}
	}
	co2, _ := strconv.ParseInt(left[0], 2, 32)
	fmt.Println(oxygen * co2)
}
