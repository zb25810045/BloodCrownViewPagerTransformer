# BloodCrownViewPagerTransformer
这是关于viewpager 切换效果实现的smaple   [本偏博客地址](http://www.jianshu.com/p/d41d2894c757)

![ezgif.com-video-to-gif.gif](http://upload-images.jianshu.io/upload_images/1785445-fe9febf1d1704c91.gif?imageMogr2/auto-orient/strip)

### 实现原理：

大家看着像是用动画实现的，其实很简单，google已经给我们做好准备了，实现 ViewPager.PageTransformer 这个接口，然后把这个实现对象设置到viewpager里即可。
```java

// 这是实现页面切换接口的实现类类
public class AlpheScaleTransformer implements ViewPager.PageTransformer {

      @Override
    public void transformPage(View page, float position) {
      ....
    }

}

// 然后把这个实现对象设置到viewpager里
 mViewPager.setPageTransformer(true, new AlpheScaleTransformer());

```

看着是不是很简单啊，其实细细想来，这和实现动画是一个原理。何为动画，快速切换的静态图片罢了，在我们切换viewpager页面时，页面随着手指滚动，滚动一次，viewpager就会调一次 ViewPager.PageTransformer 这个接口实现类来重置当前页和下一页的样式，在这里我们做缩放，透明度等各种变换来操作view的各种属性，然后随着系统16ms刷新一帧，也就成了我们看到的样子。google已经提供了这个接口，而且还放出了官方的demo，所以大家不要怕，这个切换的知识点很简单的，所以我再viewpager的第一篇才来说这块。

### google官方demo：
```java
package com.example.zb.bloodcrownviewpagertransformer.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class GooglePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            page.setAlpha(1 - position);

            // Counteract the default slide transition
            page.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }
}
```

***

### 我的实现：
```java
 public class AlpheScaleTransformer implements ViewPager.PageTransformer {

    private float minAlphe = 0.3f;
    private float minScale = 0.7f;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1 || position > 1) {
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);
            return;
        }

        if (position >= -1 && position <= 0) {
            return;
        }

        if (position > 0 && position <= 1) {
            page.setAlpha(minAlphe + (1 - minAlphe) * (1 - Math.abs(position)));
            page.setScaleX(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setScaleY(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setPivotX(page.getWidth() / 2);
            page.setPivotY(page.getHeight() / 2);
            page.setTranslationX(page.getWidth() * -position);
        }
    }
}
```

***

### 实现要点

目标接口：
```java
public class AlpheScaleTransformer implements ViewPager.PageTransformer {

      @Override
    public void transformPage(View page, float position) {
      ....
    }

}
```

上面的热闹看完了，那么我们来具体的说下实现的要点：
* 接口参数介绍
* 接口被谁调用，调用次数
* 结合实际理解接口参数

#### 接口参数介绍

ViewPager.PageTransformer  接口里面只有一个方法，用来不停刷新页面样式的。那么我们观看里面的 transformPage 这个方法，参数是一个view和一个数值。view自然是viewpager的每个页面了，position则是当前这个view页面所处的位置。

####  接口被谁调用，调用次数

那么大伙想一想我们滑动viewpager时是几个页面在动？咱就说一般的viewpager，那么发生变化的页面一个是当前页，一个是下一页或是上一页啦，那么算下来就是2个页面，就是2个view啦。这么说的话2个页面都是同时再运动，那么 transformPage(View page, float position) 这个方法必然也是会被调用多次了。会调用几次，被谁调用？也许大家会猜测是参与运动变化的这2个页面。经过测试发现不仅仅是这2个页面会调用这个接口方法，而是viewpager适配器中所有缓存的view都会调用这个接口方法，大家想过没有，为什么会是这样，因为viewpager不知道缓存的view是不是正在显示，所以干脆缓存的所有view都去做页面变换，万一都在显示呢，举个例子，大家都看过同时显示3个view的viewpager吧

#### 结合实际理解接口参数

 transformPage(View page, float position) 里面就2个参数，view大家都明白了吧，每个缓存的view都会跑一次这个方法。position比较麻烦也是这个方法的核心，所以要重点细说。上面说position是描述页面所处的位置，在开始变换前，中间的页面（也是当前页面）position是0，左边的页面position是-1，右边的页面position是1。记住中间页面position永远是0，整数表示当前页右边的页，负数表示当前页左边的页

举个例子，当前页是第二页，我们左滑，实际是第二页往左运动到手机尽头不显示，第三页从右边慢慢进入到手机屏幕代替第二页的位置。postion的变化：
* 第二页（原当前页） 0 ~ -1
* 第三页1 ~ 0

第二页从当前页运动到屏幕左侧，所以是 从0 ~ -1 的变化。
第三页因为是在第二页的右边，所以是 从1 ~ 0的变化。

大家想想是不是这样，整数表示右边，第三页的确是在第二页的右边，所以第三页开始position是1 ，中间的当前页因为是position0，所以第三页运动到屏幕中间代码第二页时，position就是0啦。第二页呢因为是中间页，开始position是0，第二页向左运动，因为第二页在第三页的左边，所以运动结束时是-1。

大家来看张position变换的表：(这张图是我扒来的)
![猎豹截图20170901013453.png](http://upload-images.jianshu.io/upload_images/1785445-ee27cc325355f5d0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

看上面这张图，基本说明了position的数值范围，position都是以1做为整数变更的，0是中间页面，-1是左边的页面，那么-2就是左边的左边，正数同理。一般viewpager的页面切换至涉及到2个页面，取值 < -1的，> 1的一般都是不可见的页面，负数都是表示左边的页面或是往左边一栋的页面，正数表示右边的页面或是往右边一栋的页面，这就是viewpager页面切换的核心，参透position参数的变化，说道这里大伙看看上面google的官方实现或是我的现实都可以，我的实现里position < -1 的我都没有操作，所以在效果图里，左边的页面除了默认的位移什么效果都没有，变化都是来自于右边就是因为position的取舍。默认都是会带上位移的，我们给view一个反向的位移就可以让view实现在原地变换的效果了。
```java
 if (position > 0 && position <= 1) {
            page.setAlpha(minAlphe + (1 - minAlphe) * (1 - Math.abs(position)));
            page.setScaleX(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setScaleY(minScale + (1 - minScale) * (1 - Math.abs(position)));
            page.setPivotX(page.getWidth() / 2);
            page.setPivotY(page.getHeight() / 2);
            page.setTranslationX(page.getWidth() * -position);  // 这行就是加一个反响的位移
        }
```

### 总结

上面我就是说了下position的数值变化，其他的没怎么说，各位看官要是理解的不是很透的话请看参考资料，尤其是参考资料里 transformer 变换的库（ViewPagerTransforms），transformer变化的核心都在这些transformer的实现类上面了。

另外我们在自己计算view属性变换的公式时，只要计算好一个方向的公式就好了，反方向postion的数值会走反向变化。

***

### 参考资料

* [ViewPager PageTransformer探索](http://www.jianshu.com/p/11a819bc5973)
* [[Android 实现个性的ViewPager切换动画 实战PageTransformer（兼容Android3.0以下）](http://blog.csdn.net/lmj623565791/article/details/40411921)](http://blog.csdn.net/lmj623565791/article/details/40411921)
* [[Android 自定义 ViewPager 打造千变万化的图片切换效果](http://blog.csdn.net/lmj623565791/article/details/38026503)](http://blog.csdn.net/lmj623565791/article/details/38026503)

***

### 第三方变换库

* [ViewPagerTransforms](https://github.com/ToxicBakery/ViewPagerTransforms)
