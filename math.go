package main

func min(vars ...int64) int64 {
	result := vars[0]
	for _, i := range vars {
		if result > i {
			result = i
		}
	}
	return result
}

func abs(a int64) int64 {
	if a < 0 {
		return -a
	}
	return a
}

func max(vars ...int64) int64 {
	result := vars[0]
	for _, i := range vars {
		if result < i {
			result = i
		}
	}
	return result
}

func minSlice(s []int64) int64 {
	result := s[0]
	for _, i := range s {
		if result > i {
			result = i
		}
	}
	return result
}

func maxSlice(s []int64) int64 {
	result := s[0]
	for _, i := range s {
		if result < i {
			result = i
		}
	}
	return result
}

func sumSlice(s []int64) int64 {
	result := int64(0)
	for _, i := range s {
		result += i
	}
	return result
}
