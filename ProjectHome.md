导出excel文件为html格式，同时精确保持原有的外观。

当前处于原型阶段。

# reademe #

features:

1 export excel use apache poiPorject

can process these format

TODO
  1. ShrinkToFit
  1. text justify patch
  1. edit online

## not support current 当前无法支持的样式 ##
  1. vertical justify
  1. slash border 斜线
  1. under line style double
  1. dash\_dot\_dot dash\_dot
  1. graph
  1. chart
  1. olap table

## already support style 支持的样式 ##

### Align ###



| align | IE6 | IE7 | chrome | FF |comment |
|:------|:----|:----|:-------|:---|:-------|
| left,right,center | ok | ok | ok | ok |  |
| justify | ok | ok | ok | ok |  |
| center selection(跨列居中) | ok | ok | ok | ok |  |
| distribute align(分散对齐)| ok | ok | no(use justify) | no(use justify) |  |
| align fill (填充对齐)| ok | ok | ok | ok | set nowrap and white-space:per |

### text rotate ###

| text rotate | IE6 | IE7,8 | chrome | FF |comment |
|:------------|:----|:------|:-------|:---|:-------|
| -90 ,90 degree | no | no | ok(-webkit-transform) | ok(-moz-transform) | use css3 transform:rotate|
| text flow tb-lr | ok(writing-mode:tb-lr) | ok(writing-mode:tb-lr) | no(normal) | no(normal)  |  |


### forgound color ###

all borswer support.

### border ###

| border | IE6 | IE7,8 | chrome | FF |comment |
|:-------|:----|:------|:-------|:---|:-------|
| solid double dotted(3px) dashed | ok | ok | ok | ok | ok |
| thin(0.5pt) medium(1pt) thick(1.5pt) | ok | ok | ok | ok | ok |
| merged cell border | ok | ok | ok | ok | perfect border style expor |

### cell merge model ###
  1. support cell merge
  1. recalculate merged cell with and height

### fonts ###
  1. font family
  1. font weight
  1. font italic
  1. color
  1. single underline
  1. double underline and any underline for  accounting map to(single underline);
  1. line-through
  1. sub and sup (use standard html tag)

### formula ###

  1. can export formual

### cell value format ###

  1. support by apachepoi

### cursor ###
| curosr | IE6 | IE7,8 | chrome | FF |comment |
|:-------|:----|:------|:-------|:---|:-------|
| cell| no(normal) | no(normal) | ok | ok | ok |

### hidden row and hidden column ###

  1. all support use display none;

## how to ##
hg clone and run test case

must import jdk1.6 poi 3.6 and junit4