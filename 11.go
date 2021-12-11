package main

import (
	"bufio"
	"fmt"
	"os"
)

var energy [][]int

func print() {
	for i := 1; i < len(energy)-1; i++ {
		fmt.Println(energy[i][1 : len(energy[i])-1])
	}
}

func step() int {
	for i := 1; i < len(energy)-1; i++ {
		for j := 1; j < len(energy[i])-1; j++ {
			energy[i][j]++
		}
	}
	flashes := 0
l:
	for i := 1; i < len(energy)-1; i++ {
		for j := 1; j < len(energy[i])-1; j++ {
			if energy[i][j] > 9 {
				flashes++
				energy[i][j] = -1000
				energy[i][j-1]++
				energy[i][j+1]++
				energy[i-1][j]++
				energy[i+1][j]++
				energy[i-1][j-1]++
				energy[i+1][j+1]++
				energy[i+1][j-1]++
				energy[i-1][j+1]++
				goto l
			}
		}
	}
	for i := 1; i < len(energy)-1; i++ {
		for j := 1; j < len(energy[i])-1; j++ {
			if energy[i][j] < 0 {
				energy[i][j] = 0
			}
		}
	}
	return flashes
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	size := len(scanner.Text())
	energy = make([][]int, size+2)
	for i := 0; i < size+2; i++ {
		energy[i] = make([]int, size+2)
		for j := 0; j < len(energy[i]); j++ {
			energy[i][j] = -1000
		}
	}
	for i, c := range scanner.Text() {
		energy[1][i+1] = int(c - rune('0'))
	}
	for i := 2; scanner.Scan(); i++ {
		for j, c := range scanner.Text() {
			energy[i][j+1] = int(c - rune('0'))
		}
	}
	flashes := 0
	st := 1
	for st <= 100 {
		flashes += step()
		st++
	}
	fmt.Println(flashes)
	for step() < size*size {
		st++
	}
	fmt.Println(st)
}
