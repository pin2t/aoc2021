package main

import (
	"bufio"
	"encoding/hex"
	"fmt"
	"math"
	"os"
	"strconv"
)

type packet struct {
	version, id int
	value       int64
	sub         []packet
}

func parse(input string, pos int) (packet, int) {
	start := pos
	version, _ := strconv.ParseInt(input[pos:pos+3], 2, 0)
	id, _ := strconv.ParseInt(input[pos+3:pos+6], 2, 0)
	if id == 4 {
		s := ""
		for pos += 6; input[pos] == '1'; pos += 5 {
			s = s + input[pos+1:pos+5]
		}
		value, _ := strconv.ParseInt(s+input[pos+1:pos+5], 2, 0)
		return packet{int(version), int(id), value, make([]packet, 0)}, pos + 5 - start
	} else if input[pos+6] == '0' {
		pos += 7
		total, _ := strconv.ParseInt(input[pos:pos+15], 2, 0)
		pos += 15
		packets := make([]packet, 0)
		for total > 0 {
			sp, len := parse(input, pos)
			packets = append(packets, sp)
			total -= int64(len)
			pos += len
		}
		return packet{int(version), int(id), 0, packets}, pos - start
	} else {
		pos += 7
		n, _ := strconv.ParseInt(input[pos:pos+11], 2, 0)
		pos += 11
		packets := make([]packet, 0)
		for i := 0; i < int(n); i++ {
			sp, len := parse(input, pos)
			packets = append(packets, sp)
			pos += len
		}
		return packet{int(version), int(id), 0, packets}, pos - start
	}
}

func (p packet) versions() int {
	result := p.version
	for _, sp := range p.sub {
		result += sp.versions()
	}
	return result
}

func (p packet) print(prefix string) {
	fmt.Print(prefix, p.version, " ")
	switch p.id {
	case 0:
		fmt.Println("+")
	case 1:
		fmt.Println("*")
	case 2:
		fmt.Println("min")
	case 3:
		fmt.Println("max")
	case 4:
		fmt.Println(p.value)
	case 5:
		fmt.Println(">")
	case 6:
		fmt.Println("<")
	case 7:
		fmt.Println("==")
	}
	for _, sp := range p.sub {
		sp.print(prefix + ".")
	}
}

func (p packet) result() int {
	r := 0
	switch p.id {
	case 0:
		for _, sp := range p.sub {
			r += sp.result()
		}
	case 1:
		r = 1
		for _, sp := range p.sub {
			r *= sp.result()
		}
	case 2:
		r = math.MaxInt
		for _, sp := range p.sub {
			if sp.result() < r {
				r = sp.result()
			}
		}
	case 3:
		r = 0
		for _, sp := range p.sub {
			if sp.result() > r {
				r = sp.result()
			}
		}
	case 4:
		r = int(p.value)
	case 5:
		if p.sub[0].result() > p.sub[1].result() {
			r = 1
		} else {
			r = 0
		}
	case 6:
		if p.sub[0].result() < p.sub[1].result() {
			r = 1
		} else {
			r = 0
		}
	case 7:
		if p.sub[0].result() == p.sub[1].result() {
			r = 1
		} else {
			r = 0
		}
	default:
		panic(fmt.Sprint("invalid packet id", p.id))
	}
	return r
}

func main() {
	input := ""
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	bytes, _ := hex.DecodeString(scanner.Text())
	for _, b := range bytes {
		input = input + fmt.Sprintf("%08b", b)
	}
	fmt.Println(input)
	p, _ := parse(input, 0)
	p.print("")
	fmt.Println(p.versions())
	fmt.Println(p.result())
}
