package hu.bme.aut.fogadasch.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.fogadasch.R
import hu.bme.aut.fogadasch.data.BetItemDatabase
import hu.bme.aut.fogadasch.databinding.ActivityNavBinding

class NavActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavBinding
    private lateinit var database: BetItemDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNav.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_nav)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_history
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        createNotificationChannel()
        database = BetItemDatabase.getDatabase(applicationContext)
//        thread {
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"Barca", "Real", 1.2, 3.2, 0,0, "2022.11.02", 2.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"Magyarország", "Franciaország", 5.7, 1.3, 0,0, "2021.11.29", 3.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"Liverpool", "Fradi", 2.5,  4.4,0,0, "2021.11.02", 2.9))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"Juventus", "Porto", 2.2, 4.8, 1,1, "2021.11.28", 1.1))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.BASKETBALL ,"kosar cs1", "kosar cs2", 1.2, 3.2, 22,47, "2022.06.02", 2.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.BASKETBALL ,"kis kosar cs1", "nagy kosar cs2", 4.2, 3.8, 54,32, "2022.06.02", 2.9))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.BASKETBALL ,"kosar cs11", "kosar cs22", 8.2, 1.1, 23,78, "2022.06.02", 2.3))
//        }
//        thread {
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"proba csapat1", "proba csapat2", 1.2, 1.2, 0,0, "2022.11.29", 2.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"proba csapat11", "proba csapat22", 4.2, 1.2, 0,0, "2022.10.29", 2.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"proba csapat3", "proba csapat4", 6.1, 3.7, 0,0, "2022.11.15", 2.0))
//            database.BetItemDao().insert(BetItem(null,false, BetItem.Category.FOOTBALL ,"proba csapat7", "proba csapat6", 3.8, 4.2, 0,0, "2022.11.15", 2.0))
//        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "valami1"
            val descriptionText = "leiras1"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("asd", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_nav)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }

}