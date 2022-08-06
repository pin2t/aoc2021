#!ruby
pos = $stdin.gets.strip.scan(/\d+/).map(&:to_i)
fuel, fuel2 = Array.new(pos.max, 0), Array.new(pos.max, 0)
for p in 0..pos.max
    fuel[p] = pos.map do |pp| (pp - p).abs end.sum
    fuel2[p] = pos.map do |pp| ((pp - p).abs + 1) * (pp + p - 2 * [pp, p].min) / 2 end.sum
end
puts(fuel.min, fuel2.min)