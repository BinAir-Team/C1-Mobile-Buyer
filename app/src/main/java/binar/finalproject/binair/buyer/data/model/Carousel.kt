package binar.finalproject.binair.buyer.data.model

import android.os.Parcel
import android.os.Parcelable

data class Carousel(val id: Int,  val imgUrl: Int):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(imgUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Carousel> {
        override fun createFromParcel(parcel: Parcel): Carousel {
            return Carousel(parcel)
        }

        override fun newArray(size: Int): Array<Carousel?> {
            return arrayOfNulls(size)
        }
    }
}