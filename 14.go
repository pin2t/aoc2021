package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	s := scanner.Text()
	pairs := make(map[string]int64)
	counts := make([]int64, 23)
	for i := 0; i < len(s)-1; i++ {
		pairs[s[i:i+2]]++
		counts[s[i]-byte('A')]++
	}
	counts[s[len(s)-1]-byte('A')]++
	rules := make(map[string]rune)
	scanner.Scan()
	for scanner.Scan() {
		rules[scanner.Text()[0:2]] = rune(scanner.Text()[6])
	}
	for step := 1; step <= 40; step++ {
		newpairs := make(map[string]int64)
		for pair, cnt := range pairs {
			insertion, found := rules[pair]
			if found {
				newpairs[string(pair[0])+string(insertion)] += cnt
				newpairs[string(insertion)+string(pair[1])] += cnt
				counts[byte(insertion)-byte('A')] += cnt
			}
		}
		if step == 10 {
			fmt.Println(maxSlice(counts) - minSliceGreater(counts, 0))
		}
		pairs = newpairs
	}
	fmt.Println(maxSlice(counts) - minSliceGreater(counts, 0))
}
