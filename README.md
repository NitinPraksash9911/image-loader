# image-loader-library

## Example code
ImageLoader().with(context).load(imgUrl).into(imgView)

## Steps into library:-
1. Use to load image from url into imageView
2. It also handle disk-level caching
3. It is easy to cancel the current image loading request just calling cancel() method.
  example: 
  ### val loadTask = ImageLoader().with(context).load(imgUrl).into(imgView)
  ### loadTast.cancel()
4. Resizing the image before storing into the cache
5. It uses Retrofit library for netwrok call

## Language
**KOTLIN**


