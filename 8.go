package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

func permutations(p *[]string, prefix string, s string) {
	if len(s) == 0 {
		*p = append(*p, prefix)
	} else {
		for i := 0; i < len(s); i++ {
			permutations(p, prefix+string(s[i]), s[:i]+s[i+1:])
		}
	}
}

func mapSegments(s string, mapping string) string {
	result := make([]rune, len(s))
	for i, c := range s {
		result[i] = rune(mapping[byte(c-'a')])
	}
	for i := 0; i < len(result); i++ {
		for j := i + 1; j < len(result); j++ {
			if result[j] < result[i] {
				t := result[j]
				result[j] = result[i]
				result[i] = t
			}
		}
	}
	return string(result)
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	var result1 int
	templates := []string{"abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg"}
	mappings := make([]string, 0, 1000)
	permutations(&mappings, "", "abcdefg")
	decoded := make([]int64, 0, 1000)
	for scanner.Scan() {
		parts := strings.Split(scanner.Text(), "|")
		leftFields := strings.Fields(parts[0])
		rightFields := strings.Fields(parts[1])
		for _, s := range rightFields {
			if len(s) == 2 || len(s) == 3 || len(s) == 4 || len(s) == 7 {
				result1++
			}
		}
		for _, mapping := range mappings {
			valid := make([]int, 0, 10)
			for _, segments := range leftFields {
				for i, t := range templates {
					if mapSegments(segments, mapping) == t {
						valid = append(valid, i)
					}
				}
			}
			if len(valid) == 10 {
				d := int64(0)
				for _, segments := range rightFields {
					for i, t := range templates {
						if mapSegments(segments, mapping) == t {
							d = d*10 + int64(i)
						}
					}
				}
				decoded = append(decoded, d)
			}
		}
	}
	fmt.Println(result1, sumSlice(decoded))
}
