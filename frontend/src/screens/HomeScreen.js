import { useEffect, useState } from 'react';
import {
    ActivityIndicator,
    RefreshControl,
    ScrollView,
    StyleSheet,
    Text,
    View,
} from 'react-native';
import { transactionAPI } from '../services/api';
import { storage } from '../utils/storage';

export default function HomeScreen() {
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [summary, setSummary] = useState(null);
  const [userName, setUserName] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const userData = await storage.getUserData();
      setUserName(userData?.name || 'User');

      const response = await transactionAPI.getCurrentMonthSummary();
      setSummary(response.data);
    } catch (error) {
      console.error('Error loading data:', error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const onRefresh = () => {
    setRefreshing(true);
    loadData();
  };

  if (loading) {
    return (
      <View style={styles.centerContainer}>
        <ActivityIndicator size="large" color="#4CAF50" />
      </View>
    );
  }

  return (
    <ScrollView
      style={styles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      <View style={styles.header}>
        <Text style={styles.greeting}>Hello, {userName}! 👋</Text>
        <Text style={styles.subtitle}>Here's your expense summary</Text>
      </View>

      {/* Summary Cards */}
      <View style={styles.summaryContainer}>
        <View style={[styles.card, styles.incomeCard]}>
          <Text style={styles.cardLabel}>Total Income</Text>
          <Text style={styles.cardAmount}>
            ₹{summary?.totalIncome?.toLocaleString() || '0'}
          </Text>
        </View>

        <View style={[styles.card, styles.expenseCard]}>
          <Text style={styles.cardLabel}>Total Expense</Text>
          <Text style={styles.cardAmount}>
            ₹{summary?.totalExpense?.toLocaleString() || '0'}
          </Text>
        </View>

        <View style={[styles.card, styles.savingsCard]}>
          <Text style={styles.cardLabel}>Net Savings</Text>
          <Text style={styles.cardAmount}>
            ₹{summary?.netSavings?.toLocaleString() || '0'}
          </Text>
        </View>
      </View>

      {/* Expense Breakdown */}
      {summary?.expenseBreakdown && summary.expenseBreakdown.length > 0 && (
        <View style={styles.breakdownContainer}>
          <Text style={styles.sectionTitle}>Expense Breakdown</Text>
          {summary.expenseBreakdown.map((item, index) => (
            <View key={index} style={styles.breakdownItem}>
              <Text style={styles.breakdownCategory}>{item.category}</Text>
              <Text style={styles.breakdownAmount}>
                ₹{item.totalAmount.toLocaleString()}
              </Text>
            </View>
          ))}
        </View>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  centerContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    backgroundColor: '#4CAF50',
    padding: 20,
    paddingTop: 50,
  },
  greeting: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
  },
  subtitle: {
    fontSize: 16,
    color: '#fff',
    marginTop: 5,
  },
  summaryContainer: {
    padding: 15,
  },
  card: {
    padding: 20,
    borderRadius: 10,
    marginBottom: 15,
  },
  incomeCard: {
    backgroundColor: '#4CAF50',
  },
  expenseCard: {
    backgroundColor: '#F44336',
  },
  savingsCard: {
    backgroundColor: '#2196F3',
  },
  cardLabel: {
    fontSize: 16,
    color: '#fff',
    marginBottom: 10,
  },
  cardAmount: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#fff',
  },
  breakdownContainer: {
    backgroundColor: '#fff',
    margin: 15,
    padding: 15,
    borderRadius: 10,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 15,
  },
  breakdownItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 10,
    borderBottomWidth: 1,
    borderBottomColor: '#f0f0f0',
  },
  breakdownCategory: {
    fontSize: 16,
  },
  breakdownAmount: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#F44336',
  },
});