import scala.collection.mutable.ArrayBuffer

"aa".toLowerCase()
1.to(10)
1 to 10

"aabbc"(4)
val s = "helLo"
s(4)
s.apply(4)
s.count(_.isUpper)
val x = 1
val s1 = if (x > 0) 1 else -1
val s2 = if (x > 0) "positive" else 1
val s3 = if (x > 0) 1
val s4 = if (x > 0) 1 else ()
var n = 3
val s5 = n -= 1 //赋值语句是Unit类型的，所以s5是Unit类型，它的值是(),
// 所以一般不要这样做 x = y = 1

def abs(x: Double)  = if (x > 0) x else -x
def decorate(str: String, left: String = "[", right: String ="]") = left + str + right
decorate("abc")

def sum(args: Int*) = {
  var result = 0
  for (arg <- args) {
    result += arg
  }
  result
}

val t = sum(1, 2, 3, 4)
sum(1 to 5: _*)

def box(s: String): Unit = {
  val border = "-" * (s.length + 2)
  print(f"$border%n|$s|%n$border%n")
}

box("hello")

lazy val words = scala.io.Source.fromFile("D:\\server.log").mkString
words

// ================= 集合 =====================
val names = List("Peter", "Paul", "Mary")
// map
names.map(_.toUpperCase())
// 如果函数返回的是一个集合而不是一个值
def ulcase(s: String) = Vector(s.toUpperCase(), s.toLowerCase)
val names1 = names.map(ulcase)
names.flatMap(ulcase)

val buffer = ArrayBuffer("Peter", "Paul", "Mary")
//buffer.transform(_.toUpperCase)

buffer.groupBy(_.substring(0, 1))

List(1, 7, 2, 9).reduceLeft(_ - _)
List(1, 7, 2, 9).reduceRight(_ - _)




















