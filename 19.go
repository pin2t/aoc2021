package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

type pos struct {
	x, y, z int
}

func (p pos) move(dx, dy, dz int) pos {
	return pos{p.x + dx, p.y + dy, p.z + dz}
}

func (p pos) rotate(times int) pos {
	switch times {
	case 0:
		return p
	case 1:
		return pos{-p.y, p.x, p.z}
	case 2:
		return pos{-p.x, -p.y, p.z}
	case 3:
		return pos{p.y, -p.x, p.z}
	}
	panic(fmt.Sprintf("incorrect rotation %d", times))
}

func (p pos) direct(to int) pos {
	switch to {
	case 0:
		return p
	case 1:
		return pos{p.x, -p.y, p.z}
	case 2:
		return pos{p.x, -p.z, p.y}
	case 3:
		return pos{-p.y, -p.z, p.x}
	case 4:
		return pos{-p.x, -p.z, -p.y}
	case 5:
		return pos{p.y, -p.z, -p.x}
	}
	panic(fmt.Sprintf("incorrect direction %d", to))
}

func (p pos) distance(other pos) int {
	return int(abs(int64(p.x-other.x)) + abs(int64(p.y-other.y)+abs(int64(p.z-other.z))))
}

func parse(s string) pos {
	parts := strings.Split(s, ",")
	x, _ := strconv.ParseInt(parts[0], 10, 32)
	y, _ := strconv.ParseInt(parts[1], 10, 32)
	z, _ := strconv.ParseInt(parts[2], 10, 32)
	return pos{int(x), int(y), int(z)}
}

type scanner struct {
	n       int
	p       pos
	beacons []pos
}

var reNum = regexp.MustCompile("\\-?\\d+")

func parseScanner(s *bufio.Scanner) (scanner, error) {
	line := s.Text()
	if line[0:3] != "---" {
		return scanner{}, fmt.Errorf("invalid input ", line)
	}
	matches := reNum.FindAllString(line, -1)
	n, _ := strconv.ParseInt(matches[0], 10, 32)
	beacons := []pos{}
	s.Scan()
	line = s.Text()
	for line != "" && len(line) > 0 && len(reNum.FindAllString(line, -1)) > 1 {
		beacons = append(beacons, parse(line))
		s.Scan()
		line = s.Text()
	}
	return scanner{int(n), pos{0, 0, 0}, beacons}, nil
}

func match(beacons map[pos]bool, s scanner) bool {
	for beacon, _ := range beacons {
		for _, p := range s.beacons {
			mapped := []pos{}
			fit := 0
			for _, b := range s.beacons {
				mb := b.move(beacon.x-p.x, beacon.y-p.y, beacon.z-p.z)
				mapped = append(mapped, mb)
				if beacons[mb] {
					fit++
				}
			}
			if fit >= 12 {
				for _, m := range mapped {
					beacons[m] = true
				}
				s.p = pos{beacon.x - p.x, beacon.y - p.y, beacon.z - p.z}
				return true
			}
		}
	}
	return false
}

func (s scanner) transform(rotation, direction int) scanner {
	t := scanner{s.n, s.p, []pos{}}
	for _, beacon := range s.beacons {
		t.beacons = append(t.beacons, beacon.rotate(rotation).direct(direction))
	}
	return t
}

func matchAll(beacons map[pos]bool, s scanner) bool {
	for rotation := 0; rotation < 4; rotation++ {
		for direction := 0; direction < 6; direction++ {
			ts := s.transform(rotation, direction)
			if match(beacons, ts) {
				s.p = ts.p
				return true
			}
		}
	}
	return false
}

func main() {
	s := bufio.NewScanner(os.Stdin)
	scanners := make([]scanner, 0)
	for s.Scan() {
		sc, err := parseScanner(s)
		if err != nil {
			panic(err)
		}
		scanners = append(scanners, sc)
	}
	unique := map[pos]bool{}
	for _, b := range scanners[0].beacons {
		unique[b] = true
	}
	processed := map[int]bool{0: true}
	for len(processed) < len(scanners) {
		for _, sc := range scanners {
			if !processed[sc.n] && matchAll(unique, sc) {
				processed[sc.n] = true
			}
		}
	}
	fmt.Println(len(unique))
	distance := 0
	for i := 0; i < len(scanners); i++ {
		for j := i + 1; j < len(scanners); j++ {
			distance = maxi(distance, scanners[i].p.distance(scanners[j].p))
		}
	}
	fmt.Println(distance)
}
