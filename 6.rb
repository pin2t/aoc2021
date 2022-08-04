#!ruby
pos = $stdin.gets.strip.scan(/\d+/).map(&:to_i)
counts = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
pos.each do |p| counts[p] += 1 end
def total(counts, days)
    while days > 0
        prev = counts.dup
        counts = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        counts[6], counts[8] = prev[0], prev[0]
        for i in 0..8 do counts[i] += prev[i + 1] end 
        days -= 1
    end
    return counts.sum
end    
puts(total(counts, 80), total(counts, 256))