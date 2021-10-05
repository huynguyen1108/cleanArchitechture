package com.example.basecleanarchitechturedagger.presentation.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.basecleanarchitechturedagger.R
import com.example.basecleanarchitechturedagger.domain.repository.AppSettingRepositoryInterface
import com.example.basecleanarchitechturedagger.domain.repository.TokenRepositoryInterface

class Dialog : DialogFragment(), DialogInterface.OnShowListener {

  private var onShowListener: DialogInterface.OnShowListener? = null
  private var onDismissListener: DialogInterface.OnDismissListener? = null

  companion object {
    private const val TITLE = "title"
    private const val TITLE_RES_ID = "titleResId"
    private const val MESSAGE = "message"
    private const val MESSAGE_RES_ID = "messageResId"
    private const val DRAWABLE_RES_ID = "drawableResId"
    private const val POSITIVE_LABEL = "positiveLabel"
    private const val POSITIVE_RES_ID = "positiveResId"
    private const val NEGATIVE_LABEL = "negativeLabel"
    private const val NEGATIVE_RES_ID = "negativeResId"
    private const val IS_ICON_ENABLE = "isIconEnable"

    fun createCloseDialog(
      title: String? = null,
      message: String? = null,
      positiveClickListener: DialogInterface.OnClickListener?
    ): Dialog {
      return Builder()
        .title(title)
        .message(message)
        .positiveLabel("Đóng")
        .positiveListener(positiveClickListener)
        .build()
    }

    fun createRetryDialog(
      title: String? = null,
      message: String? = null,
      positiveClickListener: DialogInterface.OnClickListener,
      negativeClickListener: DialogInterface.OnClickListener
    ): Dialog {
      return Builder()
        .title(title)
        .message(message)
        .positiveLabel("Thử lại")
        .positiveListener(positiveClickListener)
        .negativeLabel("Đóng")
        .negativeListener(negativeClickListener)
        .build()
    }

    fun createRetryDialogSingleButton(
      title: String? = null,
      message: String? = null,
      positiveClickListener: DialogInterface.OnClickListener
    ): Dialog {
      return Builder()
        .title(title)
        .message(message)
        .positiveLabel("Thử lại")
        .positiveListener(positiveClickListener)
        .build()
    }

    fun createAlertDialog(
      title: String? = null,
      message: String? = null,
      positiveLabel: String? = null,
      negativeLabel: String? = null,
      positiveClickListener: DialogInterface.OnClickListener? = null,
      negativeClickListener: DialogInterface.OnClickListener? = null
    ): Dialog {
      return Builder()
        .title(title)
        .message(message)
        .positiveLabel(positiveLabel)
        .positiveListener(positiveClickListener)
        .negativeLabel(negativeLabel)
        .negativeListener(negativeClickListener)
        .build()
    }

    fun createReviewAlertDialog(
      context: Context,
      positiveClickListener: DialogInterface.OnClickListener,
      negativeClickListener: DialogInterface.OnClickListener
    ): Dialog {
      return Builder()
        .title("Bạn có thích nó không?")
        .message("Vui lòng hợp tác đánh giá để trở thành một dịch vụ tốt hơn.")
        .positiveLabel("Xem lại")
        .positiveListener(positiveClickListener)
        .negativeLabel("Sau")
        .negativeListener(negativeClickListener)
        .isIconEnable(true)
        .build()
    }

    fun createTokenRefreshError(
      context: Context?,
      fragmentManager: FragmentManager,
      status: Int?,
      listener: TokenRefreshErrorDialog.RetryListener,
      tokenRepository: TokenRepositoryInterface,
      appSettingRepository: AppSettingRepositoryInterface
    ): Dialog? {
      return when (status) {
        400, 401, 500 -> {
          TokenRefreshErrorDialog(appSettingRepository, tokenRepository).createRefreshTokenForceLogoutDialog(context)
        }
        408, 429, 503 -> {
          TokenRefreshErrorDialog(appSettingRepository, tokenRepository).createTokenRefreshErrorDialog(
            context,
            status,
            fragmentManager,
            listener
          )
        }
        else -> {
          null
        }
      }
    }
  }

  fun show(activity: AppCompatActivity?) {
    activity?.run { show(supportFragmentManager) }
  }

  fun show(fragment: Fragment?) {
    fragment?.run { show(childFragmentManager) }
  }

  fun show(fragmentManager: FragmentManager) {
    val dialog = fragmentManager.findFragmentByTag(this::class.java.name)
    (dialog as? DialogFragment)?.dismiss()
    super.show(fragmentManager, this::class.java.name)
  }

  fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
    onShowListener = listener
  }

  fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
    onDismissListener = listener
  }

  class Builder {
    private var title: String? = null
    private var titleResId: Int? = null
    private var message: String? = null
    private var messageResId: Int? = null
    private var drawableResId: Int? = null
    private var positiveLabel: String? = null
    private var positiveResId: Int? = null
    private var negativeLabel: String? = null
    private var negativeResId: Int? = null
    private var onPositiveBuilderListener: DialogInterface.OnClickListener? = null
    private var onNegativeBuilderListener: DialogInterface.OnClickListener? = null
    private var isIconEnable: Boolean? = null

    fun title(title: String?) = apply { this.title = title }

    fun title(@StringRes titleResId: Int?) = apply { this.titleResId = titleResId }

    fun message(message: String?) = apply { this.message = message }

    fun message(@StringRes messageResId: Int?) = apply { this.messageResId = messageResId }

    fun image(@DrawableRes drawableResId: Int?) = apply { this.drawableResId = drawableResId }

    fun positiveLabel(positiveLabel: String?) = apply { this.positiveLabel = positiveLabel }

    fun positiveLabel(@StringRes positiveResId: Int?) = apply { this.positiveResId = positiveResId }

    fun negativeLabel(negativeLabel: String?) = apply { this.negativeLabel = negativeLabel }

    fun negativeLabel(@StringRes negativeResId: Int?) = apply { this.negativeResId = negativeResId }

    fun positiveListener(onPositiveListener: DialogInterface.OnClickListener?) =
      apply {
        this.onPositiveBuilderListener = onPositiveListener
      }

    fun negativeListener(onNegativeListener: DialogInterface.OnClickListener?) =
      apply {
        this.onNegativeBuilderListener = onNegativeListener
      }

    fun isIconEnable(isIconEnable: Boolean) = apply { this.isIconEnable = isIconEnable }

    fun build(): Dialog {
      return Dialog().apply {
        arguments = Bundle().apply {
          title?.let {
            putString(TITLE, it)
          }
          titleResId?.let {
            putInt(TITLE_RES_ID, it)
          }
          message?.let {
            putString(MESSAGE, it)
          }
          messageResId?.let {
            putInt(MESSAGE_RES_ID, it)
          }
          drawableResId?.let {
            putInt(DRAWABLE_RES_ID, it)
          }
          positiveLabel?.let {
            putString(POSITIVE_LABEL, it)
          }
          positiveResId?.let {
            putInt(POSITIVE_RES_ID, it)
          }
          negativeLabel?.let {
            putString(NEGATIVE_LABEL, it)
          }
          negativeResId?.let {
            putInt(NEGATIVE_RES_ID, it)
          }
          isIconEnable?.let {
            putBoolean(IS_ICON_ENABLE, it)
          }
        }

        onPositiveListener = onPositiveBuilderListener
        onNegativeListener = onNegativeBuilderListener
      }
    }
  }

  private var onPositiveListener: DialogInterface.OnClickListener? = null
  private var onNegativeListener: DialogInterface.OnClickListener? = null

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
    isCancelable = false
    val builder = requireContext().let {
      AlertDialog.Builder(it, R.style.CustomAlertDialog).apply {
        arguments?.also {arguments ->
          when {
            arguments.containsKey(TITLE) -> {
              if (arguments.getBoolean(IS_ICON_ENABLE)) {
                setCustomTitle(createIconHeader(arguments.getString(TITLE)))
              } else {
                setTitle(arguments.getString(TITLE))
              }
            }
            arguments.containsKey(TITLE_RES_ID) -> {
              setTitle(arguments.getInt(TITLE_RES_ID))
            }
            else -> {
              setTitle("")
            }
          }
          when {
            arguments.containsKey(MESSAGE) -> {
              setMessage(arguments.getString(MESSAGE))
            }
            arguments.containsKey(MESSAGE_RES_ID) -> {
              setMessage(arguments.getInt(MESSAGE_RES_ID))
            }
            else -> {
              setMessage("")
            }
          }
          when {
            arguments.containsKey(DRAWABLE_RES_ID) -> {
              ImageView(requireContext())
                .apply { setImageResource(arguments.getInt(DRAWABLE_RES_ID)) }
                .let { setView(it) }
            }
          }
          when {
            arguments.containsKey(POSITIVE_LABEL) -> {
              setPositiveButton(arguments.getString(POSITIVE_LABEL), onPositiveListener)
            }
            arguments.containsKey(POSITIVE_RES_ID) -> {
              setPositiveButton(arguments.getInt(POSITIVE_RES_ID), onPositiveListener)
            }
            else -> {
              setPositiveButton("Đóng") { _, _ ->
                // ignore
              }
            }
          }
          when {
            arguments.containsKey(NEGATIVE_LABEL) -> {
              setNegativeButton(arguments.getString(NEGATIVE_LABEL), onNegativeListener)
            }
            arguments.containsKey(NEGATIVE_RES_ID) -> {
              setNegativeButton(arguments.getInt(NEGATIVE_RES_ID), onNegativeListener)
            }
          }
        }
      }
    }
    return builder.create()
  }

  override fun onShow(dialog: DialogInterface?) {
    onShowListener?.onShow(dialog)
  }

  override fun onDismiss(dialog: DialogInterface) {
    onDismissListener?.onDismiss(dialog)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun createIconHeader(title: String?): TextView {
    val icon = resources.getDrawable(R.drawable.ic_launcher_foreground, null).apply {
      setBounds(0, 10, 220, 220)
    }
    return TextView(context).apply {
      text = title
      setPadding(0, 25, 0, 25)
      textSize = 20F
      typeface = Typeface.DEFAULT_BOLD
      setTextColor(resources.getColor(R.color.black, null))
      textAlignment = View.TEXT_ALIGNMENT_CENTER
      setCompoundDrawables(null, icon, null, null)
    }
  }
}
