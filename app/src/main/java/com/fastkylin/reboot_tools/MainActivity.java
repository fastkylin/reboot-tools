package com.fastkylin.reboot_tools;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.sax.TextElementListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {

    private Process su;
    private String ro = "mount -o ro,remount /system";
    private String rw = "mount -o rw,remount /system";
    private String ff = "%s/busybox";
    private String chmod = "chmod 0755 %s";
    private String cp = "cp -f %s %s";
    private String f = "/sdcard/busybox";
    private TextView btn_reboot;
    private TextView btn_shutdown;
    private TextView btn_reboot_recovery;
    private TextView btn_reboot_fastboot;
    private TextView btn_reboot_edl;
    private TextView btn_about;
    private TextView btn_exit;
    private TextView btn_quick;
    private TextView btn_safe;
    private TextView theme;


    DataInputStream is = null;
    Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_main);

        btn_shutdown = findViewById(R.id.btn_shutdown);
        btn_reboot = findViewById(R.id.btn_reboot);
        btn_reboot_recovery = findViewById(R.id.btn_reboot_recovery);
        btn_reboot_fastboot = findViewById(R.id.btn_reboot_fastboot);
        btn_reboot_edl = findViewById(R.id.btn_reboot_edl);
        btn_about = findViewById(R.id.btn_about);
        btn_exit = findViewById(R.id.btn_exit);
        btn_safe = findViewById(R.id.btn_reboot_safe);
        btn_quick = findViewById(R.id.btn_reboot_quick);
        theme = findViewById(R.id.btn_test);



        if (upgradeRootPermission(getPackageCodePath())) {

        } else {
            showwro();
        }

        btn_quick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "快速重启", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
        btn_safe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "重启到安全模式", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
        btn_reboot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "就是简单的重启啊", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
        btn_shutdown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "关机吗？", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
        btn_reboot_recovery.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "recovery就是恢复模式", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });
        btn_reboot_fastboot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "想线刷吗？", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });


        btn_reboot_edl.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "想进入9008吗？", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        btn_about.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "想干什么呢？", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });


        btn_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("重启？", "reboot ");
            }

        });

        btn_shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("关机？", "reboot -p");


            }
        });
        btn_reboot_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("重启到recovery模式？", "reboot recovery ");
            }
        });
        btn_reboot_fastboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("重启到bootloader？", "reboot bootloader");

            }
        });
        btn_reboot_edl.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                comfirm("重启到download？", "reboot edl");

            }

        });

        btn_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("快速重启？", "setprop ctl.restart surfaceflinger\nsetprop ctl.restart zygote\nreboot");
            }
        });
        btn_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirm("重启到安全模式？", "setprop persist.sys.safemode 1\nreboot");

            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public boolean cmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.about, null);
        builder.setView(view);
        final Dialog about = builder.create();
        about.show();
        TextView ok = view.findViewById(R.id.ok);
        TextView app_name = view.findViewById(R.id.app_name);
        app_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "reboot-tools", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        TextView app_version = view.findViewById(R.id.app_version);
        app_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "1.4", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about.dismiss();
            }
        });
        TextView chat = view.findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + 57650022 + "&version=1")));
                } catch (Exception fail) {
                    Toast toast = Toast.makeText(MainActivity.this, "未找到QQ", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    public void showwro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("这里有个问题");
        builder.setMessage("你确定授权了ROOT？\n请检查权限");
        builder.setPositiveButton("知道了",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void comfirm(String alert, final String cmd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.comfirm, null);
        builder.setView(view);
        final Dialog cus_dialog = builder.create();
        cus_dialog.show();
        TextView cont = view.findViewById(R.id.content);
        cont.setText(alert);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cmd(cmd);
                    }
                }, 3000);

            }
        });
        TextView cancle = view.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cus_dialog.dismiss();
            }
        });

    }


    private void loading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.loading, null);
        builder.setView(view);
        loading = builder.create();
        loading.show();
    }

    public static boolean upgradeRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public static boolean busyboxfile(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath + "busybox";
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }

    private void f() {
        if (!new File(this.f).exists()) {
            try {
                c("busybox", this.f);
            } catch (IOException e) {

                if (Build.VERSION.SDK_INT >= 9) {
                    startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", getPackageName(), (String) null)));
                }
            }
        }
    }

    private void seton() {
        f();
        try {
            s(this.rw);
            s(String.format(this.cp, new Object[]{this.f, this.ff}));
            s(String.format(this.chmod, new Object[]{this.ff}));
            s(this.ro);

        } catch (IOException e) {

        }
        if (new File(this.ff).exists()) {
            Toast toast = Toast.makeText(MainActivity.this, "安装成功", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "安装失败", Toast.LENGTH_SHORT);
            toast.show();
        }
        recreate();
        return;
    }

    private void s(String str) throws IOException {
        if (this.su == null) {
            this.su = Runtime.getRuntime().exec("su");
        }
        OutputStream outputStream = this.su.getOutputStream();
        outputStream.write(String.format("%s\n", new Object[]{str}).getBytes());
        outputStream.flush();
    }

    private void c(String str, String str2) throws IOException {
        File file = new File(str2);
        if (file.exists()) {
            file.delete();
        }
        new File(str2).createNewFile();
        InputStream open = getAssets().open(str);
        OutputStream fileOutputStream = new FileOutputStream(str2);
        byte[] bArr = new byte[1024];
        for (int read = open.read(bArr); read > 0; read = open.read(bArr)) {
            fileOutputStream.write(bArr, 0, read);
        }
        fileOutputStream.flush();
        open.close();
        fileOutputStream.close();
    }


}
